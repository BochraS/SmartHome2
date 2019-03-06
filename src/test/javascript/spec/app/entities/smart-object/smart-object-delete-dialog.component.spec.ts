/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CraTestModule } from '../../../test.module';
import { SmartObjectDeleteDialogComponent } from 'app/entities/smart-object/smart-object-delete-dialog.component';
import { SmartObjectService } from 'app/entities/smart-object/smart-object.service';

describe('Component Tests', () => {
    describe('SmartObject Management Delete Component', () => {
        let comp: SmartObjectDeleteDialogComponent;
        let fixture: ComponentFixture<SmartObjectDeleteDialogComponent>;
        let service: SmartObjectService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [SmartObjectDeleteDialogComponent]
            })
                .overrideTemplate(SmartObjectDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmartObjectDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmartObjectService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
